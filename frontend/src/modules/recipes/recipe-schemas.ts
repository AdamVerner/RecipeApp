import * as yup from "yup"
import { SchemaOf } from "yup"

export interface RecipeIngredientFormData {
	unit: string
	quantity: number
	grocery: string
	category: string
}

export const GrocerySchema: SchemaOf<RecipeIngredientFormData> = yup.object().shape({
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

export interface RecipeFormData {
	instructions: string
	name: string
	portions: number
	items: RecipeIngredientFormData[]
	image?: File
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
	items: yup.array().of(GrocerySchema)
		.min(1, "Recipe needs at least one ingredient")
		.required("Ingredients are required"),
	image: yup.mixed().optional()
	// image: yup.object().shape({
	// 	name: yup.string().required()
	// }).optional()
	// image: yup.array().of(
	// 	yup.object().shape({
	// 		name: yup.string().required()
	// 	})).optional()
})

export interface RecipeCommentFormData {
	text: string
}

export const RecipeCommentSchema: SchemaOf<RecipeCommentFormData> = yup.object().shape({
	text: yup.string()
		.required("Text is required")
})