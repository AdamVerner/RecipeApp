import {User} from "./user-models"
import axios from "axios"

const USER_REGISTER_URL = `${process.env.REACT_APP_API_URL}/user`
const USER_LOGIN_URL = `${process.env.REACT_APP_API_URL}/login`
const USER_DETAIL_URL = `${process.env.REACT_APP_API_URL}/user`

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

export const getUserDetail = (token: string) =>
	axios.get<User>(USER_DETAIL_URL, {headers: {"Authorization": token}})
		.then(res => res.data)

export const registerUser = (body: UserRegisterRequest) =>
	axios.post<User>(USER_REGISTER_URL, body)
		.then(res => res.data)

export const authenticateUser = (body: UserLoginRequest) =>
	axios.post<AuthToken>(USER_LOGIN_URL, body)
		.then(res => res.data)