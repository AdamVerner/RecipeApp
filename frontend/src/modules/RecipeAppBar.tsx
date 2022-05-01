import {
	AppBar,
	Button,
	Drawer,
	IconButton,
	Toolbar,
	Typography,
	styled,
	Divider,
	Box,
	Stack
} from "@mui/material"
import { Menu } from "@mui/icons-material"
import { useMemo, useState } from "react"
import { useUserLogout } from "./users/user-queries"
import { useAuthStore } from "./users/auth-store"
import { SidebarMenu } from "./SidebarMenu"
import ChevronLeftIcon from "@mui/icons-material/ChevronLeft"
import { useLocation } from "react-router-dom"
import { AppAuthRoutes } from "./AuthRouter"

const DrawerHeader = styled("div")(({ theme }) => ({
	display: "flex",
	alignItems: "center",
	padding: theme.spacing(0, 1),
	...theme.mixins.toolbar,
	justifyContent: "flex-end"
}))

const RouteHeaders: Map<string, string> = new Map([
	[AppAuthRoutes.PantryRoute, "Pantry"],
	[AppAuthRoutes.UserRecipesRoute, "My Recipes"],
	[AppAuthRoutes.AllRecipesRoute, "Browse Recipes"]
])

const DrawerWidth = 240

export const RecipeAppBar = () => {
	const { isAuthenticated } = useAuthStore()
	const { logout } = useUserLogout()
	const [showMenu, setShowMenu] = useState(true)
	const location = useLocation()

	const title = useMemo(() => {

		if (RouteHeaders.has(location.pathname)) {
			return RouteHeaders.get(location.pathname)
		}

		return "Recipe app"

	}, [location])

	const handleMenuClick = () => {
		setShowMenu(true)
	}

	return (
		<AppBar position="sticky">
			<Toolbar>
				{isAuthenticated &&
					<Drawer
						variant="persistent"
						anchor="left"
						open={showMenu}
					>
						<DrawerHeader>
							<IconButton onClick={() => setShowMenu(false)}>
								<ChevronLeftIcon/>
							</IconButton>
						</DrawerHeader>
						<Divider/>
						<Box sx={{ minWidth: DrawerWidth }}>
							<SidebarMenu/>
						</Box>
					</Drawer>
				}
				{isAuthenticated && !showMenu &&
					<IconButton
						size="large"
						edge="start"
						color="inherit"
						aria-label="menu"
						sx={{ mr: 2 }}
						onClick={handleMenuClick}
					>
						<Menu/>
					</IconButton>
				}
				<Stack sx={{ flexGrow: 1, paddingLeft: showMenu ? `${DrawerWidth}px` : 0 }}>
					<Typography variant="h5">{title}</Typography>
				</Stack>
				{isAuthenticated && <Button color="inherit" onClick={() => logout()}>Logout</Button>}
			</Toolbar>
		</AppBar>
	)
}