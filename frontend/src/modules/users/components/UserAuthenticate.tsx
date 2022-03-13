import {Paper, Grid} from "@mui/material"
import {UserLoginForm} from "./UserLoginForm"
import {UserRegisterForm} from "./UserRegisterForm"

export const UserAuthenticate = () => (
	<Grid
		container
		justifyContent="space-evenly"
		alignItems="stretch"
		spacing={4}
	>
		<Grid item xs={12} md={4} >
			<Paper>
				<UserLoginForm/>
			</Paper>
		</Grid>
		<Grid item xs={12} md={4}>
			<Paper>
				<UserRegisterForm/>
			</Paper>
		</Grid>
	</Grid>
)
