import {FC} from "react"
import {useLocation, Navigate} from "react-router-dom"
import {useUserStore} from "./users/user-store"
import {USER_AUTHENTICATE_ROUTE} from "./RootRouter"

export const AuthGuard: FC = ({ children }) => {
	const {isAuthenticated} = useUserStore()
	const location = useLocation()

	if (!isAuthenticated) {
		return <Navigate to={USER_AUTHENTICATE_ROUTE} state={{ from: location }} replace />
	}

	return <>{children}</>
}