import {Routes, Route} from "react-router-dom"
import {UserHome} from "./users/components/UserHome"
import {AuthGuard} from "./AuthGuard"

export const USER_HOME_ROUTE = "/"

export const AuthRouter = () => (
	<AuthGuard>
		<Routes>
			<Route path={USER_HOME_ROUTE} element={<UserHome/>}/>
		</Routes>
	</AuthGuard>
)
