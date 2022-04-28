import { Grid, Paper } from "@mui/material"
import { UserRegisterForm } from "./UserRegisterForm"


export const UserRegister = () => (
	<Grid
		container
		justifyContent="center"
		alignItems="center"
	>
		<Grid item xs={12} md={4} >
			<Paper>
				<UserRegisterForm />
			</Paper>
		</Grid>
	</Grid>
)