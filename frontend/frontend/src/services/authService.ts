import { api } from "@/boot/axios"
import { AxiosResponse } from "axios"

export interface LoginInteface {
  email: string
  password: string
}

export interface Token {
  tokenAccess: string
  refresh: string
}


export const login = async (login: LoginInteface): Promise<AxiosResponse<Token>> => {
  return (await api.post("auth/login", login))

}