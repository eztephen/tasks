export interface Task {
  id?: number;
  title: string;
  description: string;
  status: 'OPEN' | 'IN_PROGRESS' | 'DONE';
  dueDate: string;
}