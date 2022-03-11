import {Routes, Route} from "react-router-dom"
import {UserHome} from "./users/components/UserHome"
import {AuthGuard} from "./AuthGuard"

export enum AppAuthRoutes {
	UserHomeRoute = "/"
}

export const AuthRouter = () => (
	<AuthGuard>
		<Routes>
			<Route path={AppAuthRoutes.UserHomeRoute} element={<UserHome/>}/>
		</Routes>
	</AuthGuard>
)
