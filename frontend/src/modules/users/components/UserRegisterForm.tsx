import { useForm } from "react-hook-form"
import { Button, CircularProgress, Grid, Stack, TextField, Typography } from "@mui/material"
import { useSnackbar } from "notistack"
import { yupResolver } from "@hookform/resolvers/yup"
import { useRegisterUser } from "../user-queries"
import { UserRegisterFormData, UserRegisterSchema } from "../user-schemas"

export const UserRegisterForm = () => {
	const {
		register,
		handleSubmit,
		formState: { errors },
		reset
	} = useForm<UserRegisterFormData>({ resolver: yupResolver(UserRegisterSchema) })

	const { enqueueSnackbar } = useSnackbar()

	const { registerAsync, isLoading: isSubmitting } = useRegisterUser()

	const onSubmit = (data: UserRegisterFormData) => {
		registerAsync(data)
			.then(_ => {
				enqueueSnackbar("Registration successful", { variant: "success" })
				reset()
			})
			.catch(_ => {
				enqueueSnackbar("Registration failed", { variant: "error" })
			})
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
				<Button type="submit" variant="contained" disabled={isSubmitting}>
					{isSubmitting ? <CircularProgress size={25}/> : <>Register</>}
				</Button>
			</Stack>
		</form>
	)
}
