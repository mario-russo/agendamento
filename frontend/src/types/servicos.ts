
export interface ServiceForm {
    name: string;
    price: number;
    duration: number | null;
    color: string;
    category?: string;
    description?: string;
    status?: string;
    active?: boolean;
    id?:number;
    icon?:string;
}