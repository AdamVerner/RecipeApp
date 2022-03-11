import {User} from "./user-models"

import create from "zustand"
import {authenticateUser, getUserDetail, UserLoginRequest} from "./user-api"
import {persist} from "zustand/middleware"
import createContext from "zustand/context"
import {FC} from "react"

export interface UserStore {
	authToken?: string
	user?: User
	isAuthenticated: boolean

	authenticate(body: UserLoginRequest): Promise<void>

	logout(): void
}

const createUserStore = () => create<UserStore>(persist(
	(set, _) => ({
		authToken: undefined,
		user: undefined,
		isAuthenticated: false,

		logout: () => {
			set({authToken: undefined, user: undefined, isAuthenticated: false})
		},

		authenticate: async (body) => {
			const {token} = await authenticateUser(body)
			const user = await getUserDetail(token)
			set({authToken: token, user, isAuthenticated: true})
		}
	}),
	{
		name: "user-storage"
	}
))

const UserStoreContext = createContext<UserStore>()

export const UserStoreProvider: FC = ({ children }) => (
	<UserStoreContext.Provider createStore={createUserStore}>
		{children}
	</UserStoreContext.Provider>
)

export const useUserStore = UserStoreContext.useStore