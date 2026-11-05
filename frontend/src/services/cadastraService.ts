import { api } from "@/boot/axios"
import { AxiosResponse } from "axios"



interface Register {
    name: string
    email:string
    password: string
}

export const register = async (login: Register) => {
  return (await api.post("auth/register", login))

}