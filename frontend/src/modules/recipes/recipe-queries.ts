import { useMutation, useQuery, useQueryClient } from "react-query"
import {
	getAllRecipes,
	getGroceries,
	getGroceryCategories,
	getQuantityUnits, getRecipe,
	getUserRecipes,
	saveGrocery,
	saveRecipe,
	SaveRecipeRequest
} from "./recipe-api"
import { RecipeFormData } from "./components/RecipeCreateDialog"
import { Grocery } from "./recipe-models"

const ALL_RECIPES_QUERY_KEY = "recipes"
const USER_RECIPES_QUERY_KEY = "user/recipes"

const GROCERIES_QUERY_KEY = "groceries"
const QUANTITY_UNITS_QUERY_KEY = "quantity-units"
const GROCERY_CATEGORIES_QUERY_KEY = "categories"

export const FindGrocery = (groceries: Grocery[], name: string) => {
	const lowerCaseName = name.toLowerCase()

	return groceries.find(grocery => grocery.name.toLowerCase() == lowerCaseName)
}

export const useUserRecipes = () =>
	useQuery(USER_RECIPES_QUERY_KEY, getUserRecipes)

export const useAllRecipes = () =>
	useQuery(ALL_RECIPES_QUERY_KEY, getAllRecipes)

export const useRecipe = (id: number) =>
	useQuery([ALL_RECIPES_QUERY_KEY, id], () => getRecipe(id))

export const useGroceries = () =>
	useQuery(GROCERIES_QUERY_KEY, getGroceries)

export const useGrocery = (name: string) => {
	const { data: groceries } = useGroceries()

	return useQuery([GROCERIES_QUERY_KEY, name],
		() => FindGrocery(groceries ?? [], name),
		{
			enabled: groceries !== undefined
		})
}

export const useQuantityUnits = () =>
	useQuery(QUANTITY_UNITS_QUERY_KEY, getQuantityUnits)

export const useGroceryCategories = () =>
	useQuery(GROCERY_CATEGORIES_QUERY_KEY, getGroceryCategories)

export const useSaveGrocery = () => {
	const client = useQueryClient()

	return useMutation(saveGrocery, {
		onSuccess: async () => {
			await client.invalidateQueries(GROCERIES_QUERY_KEY)
		}
	})
}

export const useSaveRecipe = () => {
	const client = useQueryClient()

	return useMutation(saveRecipe, {
		onSuccess: async () => {
			await client.invalidateQueries(ALL_RECIPES_QUERY_KEY)
			await client.invalidateQueries(USER_RECIPES_QUERY_KEY)
		}
	})
}

export const useSaveRecipeForm = () => {

	const saveGrocery = useSaveGrocery()
	const saveRecipe = useSaveRecipe()
	const { data: groceries } = useGroceries()

	return useMutation(async (recipe: RecipeFormData) => {
		const items: {
			grocery: number
			unit: string
			quantity: number
		}[] = []

		for (const item of recipe.items) {
			const unit = item.quantityUnit
			const quantity = item.value

			const grocery = FindGrocery(groceries ?? [], item.grocery)

			if (grocery) {
				items.push({
					grocery: grocery.id,
					unit,
					quantity
				})
			} else {
				const newGrocery = await saveGrocery.mutateAsync({ name: item.grocery, category: item.category })

				items.push({
					grocery: newGrocery.id,
					unit,
					quantity
				})
			}
		}

		const saveRequest: SaveRecipeRequest = {
			name: recipe.name,
			portions: recipe.portions,
			instructions: recipe.instructions,
			items
		}

		await saveRecipe.mutateAsync(saveRequest)
	})
}
