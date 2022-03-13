import {Routes, Route} from "react-router-dom"
import {UserAuthenticate} from "./users/components/UserAuthenticate"
import {AppAuthRoutes, AuthRouter} from "./AuthRouter"


export const AppRoutes = {
	...AppAuthRoutes,
	UserAuthenticateRoute: "/auth"
}

export const RootRouter = () => (
	<Routes>
		<Route path={AppRoutes.UserAuthenticateRoute} element={<UserAuthenticate/>}/>
		<Route path="/*" element={<AuthRouter/>}/>
	</Routes>
)