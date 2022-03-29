import { useAuthStore } from "./auth-store"
import { useMutation, useQuery, useQueryClient } from "react-query"
import {
	loginWithCredentials,
	UserLoginRequest,
	authenticateToken,
	getUserDetail,
	registerUser
} from "./user-api"
import axios, { AxiosError } from "axios"
import { useEffect, useState } from "react"

const USER_QUERY_KEY = "user"

export const useAuthenticateTokenInStore = () => {
	const client = useQueryClient()
	const authStore = useAuthStore()
	const { logoutAsync } = useUserLogout()

	const { mutate, mutateAsync, ...rest } = useMutation(async () => {
		const token = authStore.authToken

		if (!token) {
			throw new Error("No token in user store")
		}

		const user = await authenticateToken(token)

		if (!user) {
			throw new Error("Failed to authenticate")
		}

		axios.defaults.headers.common["Authorization"] = token
		authStore.setIsAuthenticated(true)
	},
	{
		onSuccess: async () => {
			await client.invalidateQueries(USER_QUERY_KEY)
		},
		onError: async (_) => {
			await logoutAsync()
		}
	})

	return { authenticate: mutate, authenticateAsync: mutateAsync, ...rest }
}

export const useUser = () => useQuery(USER_QUERY_KEY, getUserDetail)


export const useUserLogin = () => {
	const authStore = useAuthStore()
	const authenticateToken = useAuthenticateTokenInStore()

	const { mutate, mutateAsync, ...rest } = useMutation(async (data: UserLoginRequest) => {
		const { token } = await loginWithCredentials(data)

		authStore.setAuthToken(token)
		await authenticateToken.authenticateAsync()
	})

	return { login: mutate, loginAsync: mutateAsync, ...rest }
}

export const useRegisterUser = () => {
	const { mutate, mutateAsync, ...rest } = useMutation(registerUser)

	return { register: mutate, registerAsync: mutateAsync, ...rest }
}

export const useUserLogout = () => {
	const client = useQueryClient()
	const authStore = useAuthStore()

	const { mutate, mutateAsync, ...rest } = useMutation(async () => {
		axios.defaults.headers.common["Authorization"] = ""
		authStore.logout()
	},
	{
		onSuccess: () => {
			client.clear()
		}
	}
	)

	return { logout: mutate, logoutAsync: mutateAsync, ...rest }
}

export const useReauthenticateTokenEffect = () => {
	const authStore = useAuthStore()
	const { authenticate, isLoading } = useAuthenticateTokenInStore()

	useEffect(() => {
		if (!authStore.isAuthenticated && authStore.authToken && !isLoading) {
			authenticate()
		}
	}, [authenticate, isLoading, authStore])
}

export const useAxiosAuthSetupEffect = () => {
	const authStore = useAuthStore()
	const { logoutAsync, } = useUserLogout()

	const [isInit, setIsInit] = useState(false)

	useEffect(() => {
		if (!isInit) {
			axios.interceptors.response.use(async (response) => {
				return response
			}, async (error: AxiosError) =>
			{
				if (error.response?.status === 403) {
					await logoutAsync()
				}
			})

			setIsInit(true)
		}
	}, [authStore, isInit, logoutAsync])
}