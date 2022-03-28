import { Routes, Route } from "react-router-dom"
import { UserAuthenticate } from "./users/components/UserAuthenticate"
import { AppAuthRoutes, AuthRouter } from "./AuthRouter"
import { useEffect, useState } from "react"
import { useUserStore } from "./users/user-store"
import { AuthGuard } from "./AuthGuard"
import axios from "axios"


export const AppRoutes = {
	...AppAuthRoutes,
	UserAuthenticateRoute: "/auth"
}

const useAxiosAuthSetupEffect = () => {
	const userStore = useUserStore()

	const [isInit, setIsInit] = useState(false)

	useEffect(() => {
		if (!isInit) {
			axios.interceptors.response.use(function (response) {
				if (response.status === 403) {
					userStore.logout()
				}

				return response
			})

			setIsInit(true)
		}
	}, [userStore, isInit])
}

const useAuthenticateTokenEffect = () => {
	const userStore = useUserStore()

	const [isAuthenticating, setIsAuthenticating] = useState(false)

	useEffect(() => {
		if (!userStore.isAuthenticated && userStore.authToken && !isAuthenticating) {
			setIsAuthenticating(true)
			userStore.authenticate()
				.finally(() => setIsAuthenticating(false))
		}
	}, [userStore, isAuthenticating])
}

export const RootRouter = () => {
	useAxiosAuthSetupEffect()
	useAuthenticateTokenEffect()

	return (
		<Routes>
			<Route path={AppRoutes.UserAuthenticateRoute} element={<UserAuthenticate/>}/>
			<Route path="/*" element={<AuthGuard><AuthRouter/></AuthGuard>}/>
		</Routes>
	)
}