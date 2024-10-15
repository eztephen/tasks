import { ComponentFixture, TestBed } from '@angular/core/testing';
import { TasksComponent } from './tasks.component';
import { TaskService } from '../services/task.service';
import { of } from 'rxjs';
import { ReactiveFormsModule } from '@angular/forms';
import { Task } from '../models/task.model';

describe('TasksComponent', () => {
  let component: TasksComponent;
  let fixture: ComponentFixture<TasksComponent>;
  let taskService: TaskService;
  let mockTaskService: any;

  const mockTasks: Task[] = [
    { id: 1, title: 'Test Task 1', description: 'Description 1', status: 'OPEN', dueDate: '2024-10-16' },
    { id: 2, title: 'Test Task 2', description: 'Description 2', status: 'IN_PROGRESS', dueDate: '2024-10-17' }
  ];

  beforeEach(() => {
    mockTaskService = jasmine.createSpyObj(['getTasks', 'createTask', 'updateTask', 'deleteTask']);

    TestBed.configureTestingModule({
      declarations: [TasksComponent],
      imports: [ReactiveFormsModule],
      providers: [
        { provide: TaskService, useValue: mockTaskService }
      ]
    });

    fixture = TestBed.createComponent(TasksComponent);
    component = fixture.componentInstance;
    taskService = TestBed.inject(TaskService);
  });

  it('should load tasks on initialization', () => {
    mockTaskService.getTasks.and.returnValue(of(mockTasks));

    component.ngOnInit();

    expect(component.tasks.length).toBe(2);
    expect(component.tasks).toEqual(mockTasks);
  });

  it('should create a new task', () => {
    const newTask: Task = { title: 'New Task', description: 'New Desc', status: 'OPEN', dueDate: '2024-10-18' };
    mockTaskService.createTask.and.returnValue(of({ ...newTask, id: 3 }));
    component.taskForm.setValue(newTask);

    component.onSubmit();

    expect(mockTaskService.createTask).toHaveBeenCalledWith(newTask);
    expect(component.taskForm.value.title).toBe('');
  });

  it('should update an existing task', () => {
    const updatedTask: Task = { id: 1, title: 'Updated Task', description: 'Updated Desc', status: 'DONE', dueDate: '2024-10-19' };
    component.editMode = true;
    component.currentTaskId = 1;
    mockTaskService.updateTask.and.returnValue(of(updatedTask));

    component.taskForm.setValue({
      title: updatedTask.title,
      description: updatedTask.description,
      status: updatedTask.status,
      dueDate: updatedTask.dueDate
    });

    component.onSubmit();

    expect(mockTaskService.updateTask).toHaveBeenCalledWith(1, updatedTask);
    expect(component.editMode).toBe(false);
  });

  it('should delete a task', () => {
    mockTaskService.deleteTask.and.returnValue(of(undefined));
    
    component.deleteTask(1);

    expect(mockTaskService.deleteTask).toHaveBeenCalledWith(1);
  });

  it('should reset form after cancelEdit', () => {
    component.editMode = true;
    component.currentTaskId = 1;

    component.cancelEdit();

    expect(component.editMode).toBe(false);
    expect(component.currentTaskId).toBeNull();
    expect(component.taskForm.value.title).toBe('');
  });
});