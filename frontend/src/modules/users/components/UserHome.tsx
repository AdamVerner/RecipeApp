import {Typography, Grid} from "@mui/material"
import {useUserStore} from "../user-store"

export const UserHome = () => {
	const {user} = useUserStore()

	return (
		<Grid container>
			<Typography>Hello {user?.firstName} {user?.lastName}</Typography>
		</Grid>
	)
}
