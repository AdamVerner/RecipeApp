import create from "zustand"
import { persist } from "zustand/middleware"
import createContext from "zustand/context"
import { FC } from "react"

export interface AuthStore {
	authToken?: string
	isAuthenticated: boolean

	setAuthToken(authToken: string): void
	setIsAuthenticated(isAuthenticated: boolean): void

	logout(): void
}

const createAuthStore = () => create<AuthStore>(persist(
	(set, _) => ({
		authToken: undefined,
		isAuthenticated: false,

		logout: () => {
			set({ authToken: undefined, isAuthenticated: false })
		},

		setAuthToken: (authToken) => set({ authToken }),

		setIsAuthenticated: (isAuthenticated) => set({ isAuthenticated })
	}),
	{
		name: "user-storage",
		partialize: state => ({ authToken: state.authToken })
	}
))

const AuthStoreContext = createContext<AuthStore>()

export const UserStoreProvider: FC = ({ children }) => (
	<AuthStoreContext.Provider createStore={createAuthStore}>
		{children}
	</AuthStoreContext.Provider>
)

export const useAuthStore = AuthStoreContext.useStore