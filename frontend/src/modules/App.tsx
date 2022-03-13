import {SnackbarProvider} from "notistack"
import {RootRouter} from "./RootRouter"
import {BrowserRouter} from "react-router-dom"
import {RecipeAppBar} from "./RecipeAppBar"
import {Stack} from "@mui/material"
import {UserStoreProvider} from "./users/user-store"

export const App = () => (
	<BrowserRouter>
		<SnackbarProvider maxSnack={3}>
			<UserStoreProvider>
				<RecipeAppBar/>
				<Stack padding={4}>
					<RootRouter/>
				</Stack>
			</UserStoreProvider>
		</SnackbarProvider>
	</BrowserRouter>
)