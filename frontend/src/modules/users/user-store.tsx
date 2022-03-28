import { User } from "./user-models"

import create from "zustand"
import { authenticateUser, getUserDetail, UserLoginRequest } from "./user-api"
import { persist } from "zustand/middleware"
import createContext from "zustand/context"
import { FC } from "react"
import axios from "axios"

export interface UserStore {
	authToken?: string
	user?: User
	isAuthenticated: boolean

	login(body: UserLoginRequest): Promise<void>
	authenticate(): Promise<void>

	logout(): void
}

const createUserStore = () => create<UserStore>(persist(
	(set, get) => ({
		authToken: undefined,
		user: undefined,
		isAuthenticated: false,

		logout: () => {
			axios.defaults.headers.common["Authorization"] = ""
			set({ authToken: undefined, user: undefined, isAuthenticated: false })
		},

		login: async (body) => {
			const { token } = await authenticateUser(body)
			set({ authToken: token })
			await get().authenticate()
		},

		authenticate: async() => {
			const token = get().authToken

			if (!token) {
				throw new Error("Auth token is not defined")
			}

			axios.defaults.headers.common["Authorization"] = token

			try {
				const user = await getUserDetail()
				set({ user, isAuthenticated: true })
			}
			catch (e) {
				get().logout()
			}

		}
	}),
	{
		name: "user-storage",
		partialize: state => ({ authToken: state.authToken })
	}
))

const UserStoreContext = createContext<UserStore>()

export const UserStoreProvider: FC = ({ children }) => (
	<UserStoreContext.Provider createStore={createUserStore}>
		{children}
	</UserStoreContext.Provider>
)

export const useUserStore = UserStoreContext.useStore