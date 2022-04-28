import { FC } from "react"
import { Navigate, useLocation } from "react-router-dom"
import { useAuthStore } from "./users/auth-store"
import { AppRoutes } from "./RootRouter"
import { Backdrop, CircularProgress } from "@mui/material"

export const AuthGuard: FC = ({ children }) => {
	const userStore = useAuthStore()
	const location = useLocation()

	if (!userStore.authToken) {
		return <Navigate to={AppRoutes.UserLoginRoute} state={{ from: location }} replace/>
	}

	if (!userStore.isAuthenticated) {
		return <Backdrop open={true}><CircularProgress /></Backdrop>
	}

	return <>{children}</>
}