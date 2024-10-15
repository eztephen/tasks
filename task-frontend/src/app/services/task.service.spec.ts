import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { TaskService } from './task.service';
import { Task } from '../models/task.model';

describe('TaskService', () => {
  let service: TaskService;
  let httpMock: HttpTestingController;
  const mockTasks: Task[] = [
    { id: 1, title: 'Test Task 1', description: 'Description 1', status: 'OPEN', dueDate: '2024-10-16' },
    { id: 2, title: 'Test Task 2', description: 'Description 2', status: 'IN_PROGRESS', dueDate: '2024-10-17' }
  ];

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [TaskService]
    });
    service = TestBed.inject(TaskService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  afterEach(() => {
    httpMock.verify();
  });

  it('should retrieve all tasks', () => {
    service.getTasks().subscribe((tasks) => {
      expect(tasks.length).toBe(2);
      expect(tasks).toEqual(mockTasks);
    });

    const req = httpMock.expectOne('http://localhost:8080/tasks');
    expect(req.request.method).toBe('GET');
    req.flush(mockTasks);
  });

  it('should retrieve a task by id', () => {
    const mockTask = mockTasks[0];

    service.getTaskById(1).subscribe((task) => {
      expect(task).toEqual(mockTask);
    });

    const req = httpMock.expectOne('http://localhost:8080/tasks/1');
    expect(req.request.method).toBe('GET');
    req.flush(mockTask);
  });

  it('should create a new task', () => {
    const newTask: Task = { title: 'New Task', description: 'New Desc', status: 'OPEN', dueDate: '2024-10-18' };

    service.createTask(newTask).subscribe((task) => {
      expect(task).toEqual({ ...newTask, id: 3 });
    });

    const req = httpMock.expectOne('http://localhost:8080/tasks');
    expect(req.request.method).toBe('POST');
    req.flush({ ...newTask, id: 3 });
  });

  it('should update an existing task', () => {
    const updatedTask: Task = { id: 1, title: 'Updated Task', description: 'Updated Desc', status: 'DONE', dueDate: '2024-10-19' };

    service.updateTask(1, updatedTask).subscribe((task) => {
      expect(task).toEqual(updatedTask);
    });

    const req = httpMock.expectOne('http://localhost:8080/tasks/1');
    expect(req.request.method).toBe('PUT');
    req.flush(updatedTask);
  });

  it('should delete a task', () => {
    service.deleteTask(1).subscribe((res) => {
      expect(res).toBeUndefined();
    });

    const req = httpMock.expectOne('http://localhost:8080/tasks/1');
    expect(req.request.method).toBe('DELETE');
    req.flush({});
  });
});