
interface AppConfig {
	readonly apiUrl: string
}

export const AppConfig: AppConfig = {
	apiUrl: process.env.REACT_APP_API_URL as string
}