export interface Appointment {
  id: string;
  clientName: string;
  clientPhone: string;
  service: string;
  price: number;
  date: string;
  time: string;
  status: 'pending' | 'confirmed' | 'completed' | 'cancelled';
}

export interface DashboardStats {
  totalAppointments: number;
  totalRevenue: number;
  completedAppointments: number;
  pendingAppointments: number;
}