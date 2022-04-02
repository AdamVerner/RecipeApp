import { User } from "../users/user-models"

export interface Recipe {
	id: number
	name: string
	portions: number
	instructions: string
	averageRating: number
	currentUserRating: number
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
	unit: string
	quantity: number
}

export interface RecipeComment {
	id: number
	recipe: number
	user: User
	text: string
	created: Date
}