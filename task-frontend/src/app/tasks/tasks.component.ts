import { Component, OnInit } from '@angular/core';
import { TaskService } from '../services/task.service';
import { Task } from '../models/task.model';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';

@Component({
  selector: 'app-tasks',
  templateUrl: './tasks.component.html',
  styleUrls: ['./tasks.component.css']
})
export class TasksComponent implements OnInit {

  tasks: Task[] = [];
  taskForm: FormGroup;
  editMode: boolean = false;
  currentTaskId: number | null = null;

  constructor(private taskService: TaskService, private fb: FormBuilder) {
    this.taskForm = this.fb.group({
      title: ['', Validators.required],
      description: [''],
      status: ['OPEN', Validators.required],
      dueDate: ['', Validators.required]
    });
  }

  ngOnInit(): void {
    this.loadTasks();
  }

  loadTasks(): void {
    this.taskService.getTasks().subscribe((tasks) => {
      this.tasks = tasks;
    });
  }

  onSubmit(): void {
    if (this.editMode) {
      this.updateTask();
    } else {
      this.createTask();
    }
  }

  createTask(): void {
    this.taskService.createTask(this.taskForm.value).subscribe(() => {
      this.loadTasks();
      this.taskForm.reset({ status: 'OPEN' });
    });
  }

  updateTask(): void {
    if (this.currentTaskId !== null) {
      this.taskService.updateTask(this.currentTaskId, this.taskForm.value).subscribe(() => {
        this.loadTasks();
        this.taskForm.reset({ status: 'OPEN' });
        this.editMode = false;
        this.currentTaskId = null;
      });
    }
  }

  editTask(task: Task): void {
    this.editMode = true;
    this.currentTaskId = task.id!;
    this.taskForm.setValue({
      title: task.title,
      description: task.description,
      status: task.status,
      dueDate: task.dueDate
    });
  }

  deleteTask(id: number): void {
    this.taskService.deleteTask(id).subscribe(() => {
      this.loadTasks();
    });
  }

  cancelEdit(): void {
    this.editMode = false;
    this.taskForm.reset({ status: 'OPEN' });
    this.currentTaskId = null;
  }
}