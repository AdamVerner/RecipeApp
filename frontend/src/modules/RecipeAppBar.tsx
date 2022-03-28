import {
	AppBar,
	Button,
	Drawer,
	IconButton,
	List,
	ListItem,
	ListItemText,
	Toolbar,
	Typography,
	Box
} from "@mui/material"
import { useUserStore } from "./users/user-store"
import { Menu } from "@mui/icons-material"
import { useState } from "react"
import { useNavigate } from "react-router-dom"
import { AppRoutes } from "./RootRouter"


export const RecipeAppBar = () => {
	const { logout, isAuthenticated } = useUserStore()
	const [showMenu, setShowMenu] = useState(false)
	const navigate = useNavigate()

	const handleMenuClick = () => {
		setShowMenu(true)
	}

	return (
		<AppBar position="sticky">
			<Toolbar>
				<Drawer
					anchor="left"
					open={showMenu}
					onClose={() => setShowMenu(false)}
				>
					<Box sx={{ width: 250 }}>
						<List>
							<ListItem button onClick={() => navigate(AppRoutes.UserRecipesRoute)}>
								<ListItemText primary="Home" />
							</ListItem>
							<ListItem button onClick={() => navigate(AppRoutes.AllRecipesRoute)}>
								<ListItemText primary="All recipes" />
							</ListItem>
						</List>
					</Box>
				</Drawer>
				{isAuthenticated &&
					<IconButton
						size="large"
						edge="start"
						color="inherit"
						aria-label="menu"
						sx={{ mr: 2 }}
						onClick={handleMenuClick}
					>
						<Menu />
					</IconButton>
				}
				<Typography variant="h4" component="div" sx={{ flexGrow: 1 }}>Recipe app</Typography>
				{isAuthenticated && <Button color="inherit" onClick={logout}>Logout</Button>}
			</Toolbar>
		</AppBar>
	)
}

