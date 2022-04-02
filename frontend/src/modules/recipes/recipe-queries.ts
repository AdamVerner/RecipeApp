import { useMutation, useQuery, useQueryClient } from "react-query"
import {
	getAllRecipes,
	getGroceries,
	getGroceryCategories,
	getQuantityUnits, getRecipe, getRecipeComments,
	getUserRecipes,
	saveGrocery,
	saveRecipe, saveRecipeComment, saveRecipeRating,
	SaveRecipeRequest
} from "./recipe-api"
import { Grocery, Recipe } from "./recipe-models"
import { GroceryFormData, RecipeFormData } from "./recipe-schemas"

const ALL_RECIPES_QUERY_KEY = "recipes"
const USER_RECIPES_QUERY_KEY = "user/recipes"

const GROCERIES_QUERY_KEY = "groceries"
const QUANTITY_UNITS_QUERY_KEY = "quantity-units"
const GROCERY_CATEGORIES_QUERY_KEY = "categories"
const RECIPE_COMMENTS_QUERY_KEY = "comments"

export const FindGrocery = (groceries: Grocery[], name: string) => {
	const lowerCaseName = name.toLowerCase()

	return groceries.find(grocery => grocery.name.toLowerCase() == lowerCaseName)
}

export const useUserRecipes = () => {
	const { data, ...rest } = useQuery(USER_RECIPES_QUERY_KEY, getUserRecipes)

	return { recipes: data ?? [], ...rest }
}

export const useAllRecipes = () => {
	const { data, ...rest } = useQuery(ALL_RECIPES_QUERY_KEY, getAllRecipes)

	return { recipes: data ?? [], ...rest }
}

export const useRecipe = (id: number) => {
	const { data, ...rest } = useQuery([ALL_RECIPES_QUERY_KEY, id], () => getRecipe(id))

	return { recipe: data, ...rest }
}


export const useGroceries = () => {
	const { data, ...rest } = useQuery(GROCERIES_QUERY_KEY, getGroceries)

	return { groceries: data ?? [], ...rest }
}


export const useGrocery = (name: string) => {
	const { groceries } = useGroceries()

	const { data, ...rest } =  useQuery([GROCERIES_QUERY_KEY, name],
		() => FindGrocery(groceries ?? [], name),
		{
			enabled: groceries !== undefined
		})

	return { grocery: data, ...rest }
}

export const useQuantityUnits = () => {
	const { data, ...rest } = useQuery(QUANTITY_UNITS_QUERY_KEY, getQuantityUnits)

	return { quantityUnits: data ?? [], ...rest }
}

export const useGroceryCategories = () => {
	const { data, ...rest } = useQuery(GROCERY_CATEGORIES_QUERY_KEY, getGroceryCategories)

	return { groceryCategories: data ?? [], ...rest }
}

export const useSaveGrocery = () => {
	const client = useQueryClient()

	const { mutate, mutateAsync, ...rest } = useMutation(saveGrocery, {
		onSuccess: async () => {
			await client.invalidateQueries(GROCERIES_QUERY_KEY)
		}
	})

	return { saveGrocery: mutate, saveGroceryAsync: mutateAsync, ...rest }
}

export const useSaveRecipe = () => {
	const client = useQueryClient()

	const { mutate, mutateAsync, ...rest } = useMutation(saveRecipe, {
		onSuccess: async () => {
			await client.invalidateQueries(ALL_RECIPES_QUERY_KEY)
			await client.invalidateQueries(USER_RECIPES_QUERY_KEY)
		}
	})

	return { saveRecipe: mutate, saveRecipeAsync: mutateAsync, ...rest }
}

export const useGetOrSaveGrocery = () => {
	const { saveGroceryAsync } = useSaveGrocery()
	const { groceries } = useGroceries()

	const { mutate, mutateAsync, ...rest } = useMutation(GROCERIES_QUERY_KEY, async (item: GroceryFormData) => {

		const unit = item.unit
		const quantity = item.quantity

		const grocery = FindGrocery(groceries, item.grocery)

		if (grocery) {

			return {
				grocery: grocery.id,
				unit,
				quantity
			}
		} else {
			const newGrocery = await saveGroceryAsync({ name: item.grocery, category: item.category })

			return {
				grocery: newGrocery.id,
				unit,
				quantity
			}
		}
	})

	return { getOrSaveGrocery: mutate, getOrSaveGroceryAsync: mutateAsync, ...rest }
}

export const useSaveRecipeForm = () => {
	const { getOrSaveGroceryAsync } = useGetOrSaveGrocery()
	const { saveRecipeAsync } = useSaveRecipe()

	const { mutate, mutateAsync, ...rest } = useMutation(async (recipe: RecipeFormData) => {
		const items = await Promise.all(recipe.items.map(async (item) => await getOrSaveGroceryAsync(item)))

		const saveRequest: SaveRecipeRequest = {
			name: recipe.name,
			portions: recipe.portions,
			instructions: recipe.instructions,
			items
		}

		return await saveRecipeAsync(saveRequest)
	})

	return { saveRecipeForm: mutate, saveRecipeFormAsync: mutateAsync, ...rest }
}

export const useRecipeComments = (recipeId: number) => {
	const { data, ...rest } = useQuery([RECIPE_COMMENTS_QUERY_KEY, recipeId], () => getRecipeComments(recipeId))

	return { recipeComments: data, ...rest }

}

export const useSaveRecipeComment = () => {
	const client = useQueryClient()

	const { mutate, mutateAsync, ...rest } = useMutation(saveRecipeComment, {
		onSuccess: async (_, request) => {
			await client.invalidateQueries([RECIPE_COMMENTS_QUERY_KEY, request.recipe])
		}
	})

	return { saveRecipeComment: mutate, saveRecipeCommentAsync: mutateAsync, ...rest }
}

const updateRecipes = (recipes: Recipe[], updatedRecipe: Recipe) => {
	const recipeIdx = recipes.findIndex(recipe => recipe.id === updatedRecipe.id)

	if (recipeIdx !== -1) {
		const updatedRecipes = [...recipes]
		updatedRecipes[recipeIdx] = updatedRecipe

		return updatedRecipes
	}

	return recipes
}

export const useSaveRecipeRating = () => {
	const client = useQueryClient()

	const { mutate, mutateAsync, ...rest } = useMutation(saveRecipeRating, {
		onSuccess: async (_, request) => {
			await client.invalidateQueries([ALL_RECIPES_QUERY_KEY, request.recipe])

			const updatedRecipe = client.getQueryData<Recipe>([ALL_RECIPES_QUERY_KEY, request.recipe])
			const recipes = client.getQueryData<Recipe[]>(ALL_RECIPES_QUERY_KEY)
			const userRecipes = client.getQueryData<Recipe[]>(USER_RECIPES_QUERY_KEY)

			if (!updatedRecipe) {
				return
			}

			if (recipes) {
				client.setQueryData<Recipe[]>(ALL_RECIPES_QUERY_KEY, updateRecipes(recipes, updatedRecipe))
			}

			if (userRecipes) {
				client.setQueryData<Recipe[]>(USER_RECIPES_QUERY_KEY, updateRecipes(userRecipes, updatedRecipe))
			}
		}
	})

	return { saveRecipeRating: mutate, saveRecipeRatingAsync: mutateAsync, ...rest }
}