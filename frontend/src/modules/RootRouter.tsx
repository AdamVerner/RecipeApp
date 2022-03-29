import { Routes, Route } from "react-router-dom"
import { UserAuthenticate } from "./users/components/UserAuthenticate"
import { AppAuthRoutes, AuthRouter } from "./AuthRouter"
import { AuthGuard } from "./AuthGuard"
import { useAxiosAuthSetupEffect, useReauthenticateTokenEffect } from "./users/user-queries"

export const AppRoutes = {
	...AppAuthRoutes,
	UserAuthenticateRoute: "/auth"
}

export const RootRouter = () => {
	useAxiosAuthSetupEffect()
	useReauthenticateTokenEffect()

	return (
		<Routes>
			<Route path={AppRoutes.UserAuthenticateRoute} element={<UserAuthenticate/>}/>
			<Route path="/*" element={<AuthGuard><AuthRouter/></AuthGuard>}/>
		</Routes>
	)
}