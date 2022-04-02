import { List, ListItem, ListItemIcon, ListItemText } from "@mui/material"
import HomeIcon from "@mui/icons-material/Home"
import RestaurantIcon from "@mui/icons-material/Restaurant"
import SettingsIcon from "@mui/icons-material/Settings"
import PersonIcon from "@mui/icons-material/Person"
import { AppRoutes } from "./RootRouter"
import { useNavigate } from "react-router-dom"

export const SidebarMenu = () => {
	const navigate = useNavigate()

	return (
		<List>
			<ListItem button onClick={() => navigate(AppRoutes.PantryRoute)}>
				<ListItemIcon><HomeIcon /></ListItemIcon>
				<ListItemText primary="Pantry" />
			</ListItem>
			<ListItem button onClick={() => navigate(AppRoutes.UserRecipesRoute)}>
				<ListItemIcon><RestaurantIcon /></ListItemIcon>
				<ListItemText primary="My recipes" />
			</ListItem>
			<ListItem button onClick={() => navigate(AppRoutes.AllRecipesRoute)}>
				<ListItemIcon><RestaurantIcon /></ListItemIcon>
				<ListItemText primary="Browse recipes" />
			</ListItem>
			<ListItem button>
				<ListItemIcon><PersonIcon /></ListItemIcon>
				<ListItemText primary="Profile" />
			</ListItem>
			<ListItem button>
				<ListItemIcon><SettingsIcon /></ListItemIcon>
				<ListItemText primary="Settings" />
			</ListItem>
		</List>
	)
}