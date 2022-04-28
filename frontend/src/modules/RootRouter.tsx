import { Routes, Route } from "react-router-dom"
import { UserLogin } from "./users/components/UserLogin"
import { AppAuthRoutes, AuthRouter } from "./AuthRouter"
import { AuthGuard } from "./AuthGuard"
import { UserRegister } from "./users/components/UserRegister"
import { useAxiosAuthSetupEffect, useReauthenticateTokenEffect } from "./users/user-queries"

export const AppRoutes = {
	...AppAuthRoutes,
	UserLoginRoute: "/login",
	UserRegisterRoute: "/register"
}

export const RootRouter = () => {
	useAxiosAuthSetupEffect()
	useReauthenticateTokenEffect()
	
	return (
		<Routes>
			<Route path={AppRoutes.UserLoginRoute} element={<UserLogin/>}/>
			<Route path={AppRoutes.UserRegisterRoute} element={<UserRegister/>}/>
			<Route path="/*" element={<AuthGuard><AuthRouter/></AuthGuard>}/>
		</Routes>
	)
}