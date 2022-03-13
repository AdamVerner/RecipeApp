import {createTheme} from "@mui/material"
import {red} from "@mui/material/colors"

type WithUndefinedValues<T> = { [K in keyof T]: T[K] | undefined }

declare module "@mui/material/styles" {
	interface Theme {
		status: {
			danger: string
		}
	}

	// eslint-disable-next-line @typescript-eslint/no-empty-interface
	interface ThemeOptions extends WithUndefinedValues<Theme>{}
}

export const materialTheme = createTheme({
	status: {
		danger: red[500],
	}
})