import { useForm } from "react-hook-form"
import { Button, CircularProgress, Stack, TextField, Typography } from "@mui/material"
import { useNavigate } from "react-router-dom"
import { AppRoutes } from "../../RootRouter"
import { useSnackbar } from "notistack"
import { yupResolver } from "@hookform/resolvers/yup"
import * as yup from "yup"
import { useUserLogin } from "../user-queries"

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
		formState: { errors }
	} = useForm<UserLoginFormData>({ resolver: yupResolver(validationSchema) })

	const { enqueueSnackbar } = useSnackbar()

	const { loginAsync, isLoading: isSubmitting } = useUserLogin()
	const navigate = useNavigate()

	const onSubmit = (data: UserLoginFormData) => {
		loginAsync(data)
			.then(() => navigate(AppRoutes.UserRecipesRoute))
			.catch(() => enqueueSnackbar("Login failed", { variant: "error" }))
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
						label="Email"
					/>
					<TextField
						error={!!errors.password}
						helperText={errors.password?.message}
						type="password"
						{...register("password")}
						label="Password"
					/>
					<Button type="submit" disabled={isSubmitting} variant="contained">
						{isSubmitting ? <CircularProgress size={25}/> : <span>Login</span>}
					</Button>
				</Stack>
			</form>
		</>
	)
}
