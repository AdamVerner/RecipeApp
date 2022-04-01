import * as yup from "yup"
import { GroceryFormData, GrocerySchema } from "../recipes/recipe-schemas"
import { SchemaOf } from "yup"

export const PantryGrocerySchema: SchemaOf<PantryGroceryFormData> = GrocerySchema.shape({
	expiration: yup.date()
		.transform((current, input) => input === "" ? undefined : current)
		.optional()
})

export interface PantryGroceryFormData extends GroceryFormData {
	expiration?: Date
}