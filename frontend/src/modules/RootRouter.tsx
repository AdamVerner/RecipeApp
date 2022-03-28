import { Routes, Route } from "react-router-dom"
import { UserAuthenticate } from "./users/components/UserAuthenticate"
import { AppAuthRoutes, AuthRouter } from "./AuthRouter"
import { useEffect, useState } from "react"
import { useUserStore } from "./users/user-store"
import { AuthGuard } from "./AuthGuard"


export const AppRoutes = {
	...AppAuthRoutes,
	UserAuthenticateRoute: "/auth"
}

export const RootRouter = () => {
	const userStore = useUserStore()

	const [isAuthenticating, setIsAuthenticating] = useState(false)

	useEffect(() => {
		if (!userStore.isAuthenticated && userStore.authToken && !isAuthenticating) {
			setIsAuthenticating(true)
			userStore.authenticate()
				.finally(() => setIsAuthenticating(false))
		}
	}, [userStore, isAuthenticating])

	return (
		<Routes>
			<Route path={AppRoutes.UserAuthenticateRoute} element={<UserAuthenticate/>}/>
			<Route path="/*" element={<AuthGuard><AuthRouter/></AuthGuard>}/>
		</Routes>
	)
}