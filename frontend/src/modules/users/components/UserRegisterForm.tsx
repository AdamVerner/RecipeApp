import { useForm } from "react-hook-form"
import { Button, CircularProgress, Grid, Stack, TextField, Typography } from "@mui/material"
import { registerUser } from "../user-api"
import { useSnackbar } from "notistack"
import * as yup from "yup"
import { yupResolver } from "@hookform/resolvers/yup"
import { useState } from "react"

interface UserRegisterFormData {
	email: string
	password: string
	firstName: string
	lastName: string
}

export const UserRegisterForm = () => {
	const [isBusy, setIsBusy] = useState(false)

	const validationSchema = yup.object().shape({
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

	const {
		register,
		handleSubmit,
		formState: { errors },
		reset
	} = useForm<UserRegisterFormData>({ resolver: yupResolver(validationSchema) })

	const { enqueueSnackbar } = useSnackbar()

	const onSubmit = (data: UserRegisterFormData) => {
		setIsBusy(true)

		registerUser(data)
			.then(_ => {
				enqueueSnackbar("Registration successful", { variant: "success" })
				reset()
			})
			.catch(_ => {
				enqueueSnackbar("Registration failed", { variant: "error" })
			})
			.finally(() => setIsBusy(false))
	}

	return (
		<form onSubmit={handleSubmit(onSubmit)}>
			<Stack spacing={2} padding={3} alignItems="center">
				<Grid item>
					<Typography align="center" variant="h5">Register</Typography>
				</Grid>
				<TextField error={!!errors.email}
						   helperText={errors.email?.message}
						   {...register("email")}
						   label="Email"
				/>
				<TextField error={!!errors.password}
						   helperText={errors.password?.message}
						   type="password"
						   {...register("password")}
						   label="Password"
				/>
				<TextField error={!!errors.firstName}
						   helperText={errors.firstName?.message}
						   {...register("firstName")}
						   label="Firstname"
				/>
				<TextField error={!!errors.lastName}
						   helperText={errors.lastName?.message}
						   {...register("lastName")}
						   label="Lastname"
				/>
				<Button type="submit" variant="contained" disabled={isBusy}>
					{isBusy ? <CircularProgress size={25}/> : <>Register</>}
				</Button>
			</Stack>
		</form>
	)
}
