import * as yup from "yup"
import { SchemaOf } from "yup"

export const PantryGrocerySchema: SchemaOf<PantryGroceryFormData> = yup.object().shape({
	unit: yup.string()
		.transform((current, input) => !input ? "" : current)
		.optional(),
	quantity: yup.number()
		.transform((current, input) => input === "" ? undefined : current)
		.moreThan(0, "Quantity must be higher than 0")
		.optional(),
	grocery: yup.string()
		.transform((current, input) => !input ? "" : current)
		.required("Grocery is required"),
	category: yup.string()
		.transform((current, input) => !input ? "" : current)
		.required("Category is required"),
	expiration: yup.date()
		.transform((current, input) => input === "" ? undefined : current)
		.optional()
})

export interface PantryGroceryFormData {
	unit?: string
	quantity?: number
	grocery: string
	category: string
	expiration?: Date
}