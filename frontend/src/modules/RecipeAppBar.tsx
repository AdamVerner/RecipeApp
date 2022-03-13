import {AppBar, Button, Toolbar, Typography} from "@mui/material"
import {useUserStore} from "./users/user-store"


export const RecipeAppBar = () => {
	const {logout, isAuthenticated} = useUserStore()

	return (
		<AppBar position="sticky">
			<Toolbar>
				<Typography variant="h4" component="div" sx={{flexGrow: 1}}>Recipe app</Typography>
				{isAuthenticated && <Button color="inherit" onClick={logout}>Logout</Button>}
			</Toolbar>
		</AppBar>
	)
}

