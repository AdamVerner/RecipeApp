import { User } from "./user-models"
import axios from "axios"
import { AppConfig } from "../app-config"

const USER_REGISTER_URL = `${AppConfig.apiUrl}/user`
const USER_LOGIN_URL = `${AppConfig.apiUrl}/login`
const USER_DETAIL_URL = `${AppConfig.apiUrl}/user`

export interface UserLoginRequest {
	email: string
	password: string
}

export interface UserRegisterRequest {
	email: string
	password: string
	firstName: string
	lastName: string
}

export interface AuthToken {
	token: string
}

export const getUserDetail = () =>
	axios.get<User>(USER_DETAIL_URL)
		.then(res => res.data)

export const authenticateToken = (token: string) =>
	axios.get<User>(USER_DETAIL_URL, { headers: { "Authorization": token } })
		.then(res => res.data)


export const registerUser = (body: UserRegisterRequest) =>
	axios.post<User>(USER_REGISTER_URL, body)
		.then(res => res.data)

export const loginWithCredentials = (body: UserLoginRequest) =>
	axios.post<AuthToken>(USER_LOGIN_URL, body)
		.then(res => {
			return res.data
		})