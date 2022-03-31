import { Autocomplete } from "@mui/material"
import { Control, Controller, FieldValues, Path } from "react-hook-form"
import { ReactNode } from "react"

interface ControlledAutocompleteProps<T extends FieldValues> {
	control: Control<T>
	name: Path<T>
	options: unknown[]
	renderInput: (params: object) => ReactNode
	getOptionLabel?: (obj: unknown) => string
	disabled?: boolean
	defaultValue?: unknown
	freeSolo?: boolean
	autoSelect?: boolean
}

export const ControlledAutocomplete = <T,>({ options, renderInput, name, control, disabled, defaultValue, getOptionLabel, freeSolo, autoSelect }: ControlledAutocompleteProps<T>) => {
	return (
		<Controller
			control={control}
			name={name}
			render={({ field: { onChange, value, ...props } }) => (
				<Autocomplete
					options={options}
					disabled={disabled}
					getOptionLabel={getOptionLabel}
					freeSolo={freeSolo}
					autoSelect={autoSelect}
					noOptionsText=""
					renderInput={renderInput}
					onChange={(_, value) => onChange(value)}
					defaultValue={defaultValue}
					value={value ?? null}
					{...props}
				/>
			)}
		/>
	)
}
