import { Paper, Grid } from "@mui/material"
import { UserLoginForm } from "./UserLoginForm"

export const UserLogin = () => (
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
	</Grid>
)
