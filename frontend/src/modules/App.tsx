import { SnackbarProvider } from "notistack"
import { RootRouter } from "./RootRouter"
import { BrowserRouter } from "react-router-dom"
import { RecipeAppBar } from "./RecipeAppBar"
import { Stack, ThemeProvider } from "@mui/material"
import { UserStoreProvider } from "./users/auth-store"
import { materialTheme } from "./styles/material-theme"
import { Global } from "@emotion/react"
import { globalStyle } from "./styles/global-style"
import ModalProvider from "mui-modal-provider"
import { QueryClient, QueryClientProvider } from "react-query"

const queryClient = new QueryClient()

export const App = () => {
	const appContent = (
		<>
			<RecipeAppBar/>
			<Stack padding={4}>
				<RootRouter/>
			</Stack>
		</>
	)

	return (
		<ThemeProvider theme={materialTheme}>
			<Global styles={globalStyle}/>
			<BrowserRouter>
				<SnackbarProvider maxSnack={3}>
					<UserStoreProvider>
						<QueryClientProvider client={queryClient} >
							<ModalProvider>
								{appContent}
							</ModalProvider>
						</QueryClientProvider>
					</UserStoreProvider>
				</SnackbarProvider>
			</BrowserRouter>
		</ThemeProvider>
	)
}