import {Routes, Route} from "react-router-dom"
import {UserAuthenticate} from "./users/components/UserAuthenticate"
import {AuthRouter} from "./AuthRouter"

export const USER_AUTHENTICATE_ROUTE = "/auth"

export const RootRouter = () => (
	<Routes>
		<Route path={USER_AUTHENTICATE_ROUTE} element={<UserAuthenticate/>}/>
		<Route path="/*" element={<AuthRouter/>}/>
	</Routes>
)