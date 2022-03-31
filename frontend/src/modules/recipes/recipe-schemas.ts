import * as yup from "yup"
import { SchemaOf } from "yup"

export const GrocerySchema: SchemaOf<GroceryFormData> = yup.object().shape({
	unit: yup.string()
		.transform((current, input) => !input ? "" : current)
		.required("Unit is required"),
	quantity: yup.number()
		.required("Quantity is required")
		.typeError("You must enter a number")
		.moreThan(0, "Quantity must be higher than 0"),
	grocery: yup.string()
		.transform((current, input) => !input ? "" : current)
		.required("Grocery is required"),
	category: yup.string()
		.transform((current, input) => !input ? "" : current)
		.required("Category is required")
})

export interface GroceryFormData {
	unit: string
	quantity: number
	grocery: string
	category: string
}

export const RecipeSchema: SchemaOf<RecipeFormData> = yup.object().shape({
	name: yup.string()
		.required("Name is required"),
	instructions: yup.string()
		.required("Instructions are required"),
	portions: yup.number()
		.required("Portions are required")
		.min(1, "Recipe need to have at least one portion")
		.integer("Portions can't be fractional")
		.typeError("You must enter a number"),
	items: yup.array().of(GrocerySchema).optional()
})

export interface RecipeFormData {
	name: string
	instructions: string
	portions: number
	items: GroceryFormData[]
}