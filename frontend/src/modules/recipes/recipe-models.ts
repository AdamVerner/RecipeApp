import { User } from "../users/user-models"

export interface Recipe {
	id: number
	name: string
	portions: number
	instructions: string
	user: User
	items: RecipeItem[]
}

export type GroceryCategory = string

export interface Grocery {
	id: number
	name: string
	category: GroceryCategory
}

export interface RecipeItem {
	grocery: number
	quantity: number
	unit: string
}