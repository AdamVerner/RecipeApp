import * as yup from "yup"
import { SchemaOf } from "yup"

export interface UserLoginFormData {
	email: string
	password: string
}

export const UserLoginSchema: SchemaOf<UserLoginFormData> = yup.object().shape({
	email: yup.string()
		.required("Email is required")
		.email("Email is invalid"),
	password: yup.string()
		.required("Password is required")
})

export interface UserRegisterFormData {
	email: string
	password: string
	firstName: string
	lastName: string
}

export const UserRegisterSchema: SchemaOf<UserRegisterFormData> = yup.object().shape({
	email: yup.string()
		.required("Email is required")
		.email("Email is invalid"),
	password: yup.string()
		.required("Password is required"),
	firstName: yup.string()
		.required("Firstname is required"),
	lastName: yup.string()
		.required("Lastname is required")
})

