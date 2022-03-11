import {useForm} from "react-hook-form"
import {Button, Stack, TextField, Typography} from "@mui/material"
import {useUserStore} from "../user-store"
import {useNavigate} from "react-router-dom"
import {AppRoutes} from "../../RootRouter"
import {useSnackbar} from "notistack"
import {yupResolver} from "@hookform/resolvers/yup"
import * as yup from "yup"

interface UserLoginFormData {
	email: string
	password: string
}

export const UserLoginForm = () => {
	const validationSchema = yup.object().shape({
		email: yup.string()
			.required("Email is required")
			.email("Email is invalid"),
		password: yup.string()
			.required("Password is required")
	})

	const {
		register,
		handleSubmit,
		formState: {errors}
	} = useForm<UserLoginFormData>({resolver: yupResolver(validationSchema)})

	const {enqueueSnackbar} = useSnackbar()

	const {authenticate} = useUserStore()
	const navigate = useNavigate()

	const onSubmit = (data: UserLoginFormData) => {
		authenticate(data)
			.then(() => {
				navigate(AppRoutes.UserHomeRoute)
			})
			.catch(() => {
				enqueueSnackbar("Login failed", {variant: "error"})
			})
	}

	return (
		<>
			<form onSubmit={handleSubmit(onSubmit)}>
				<Stack spacing={2} padding={3} alignItems="center">
					<Typography align="center" variant="h5">Login</Typography>
					<TextField
						error={!!errors.email}
						helperText={errors.email?.message}
						{...register("email")}
						label="Email"/>
					<TextField
						error={!!errors.password}
						helperText={errors.password?.message}
						type="password"
						{...register("password")}
						label="Password"/>
					<Button type="submit" variant="contained">Login</Button>
				</Stack>
			</form>
		</>
	)
}
