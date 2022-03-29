import { AppConfig } from "../app-config"
import axios from "axios"
import { Grocery, GroceryCategory, Recipe } from "./recipe-models"

const GET_ALL_RECIPES_URL = `${AppConfig.apiUrl}/recipes-all`
const GET_RECIPE_URL = `${AppConfig.apiUrl}/recipe`
const GET_USER_RECIPES_URL = `${AppConfig.apiUrl}/recipes`
const SAVE_RECIPE_URL = `${AppConfig.apiUrl}/recipe`

const GET_GROCERY_CATEGORIES_URL = `${AppConfig.apiUrl}/grocery-categories`
const GET_GROCERIES_URL = `${AppConfig.apiUrl}/groceries`
const SAVE_GROCERY_URL = `${AppConfig.apiUrl}/grocery`

const GET_QUANTITY_UNITS_URL = `${AppConfig.apiUrl}/quantity-units`

export interface SaveRecipeRequest {
	name: string
	portions: number
	instructions: string
	items: {
		grocery: number
		unit: string
		quantity: number
	}[]
}

export interface SaveGroceryRequest {
	name: string
	category: string
}

export const getQuantityUnits = () =>
	axios.get<string[]>(GET_QUANTITY_UNITS_URL)
		.then(res => res.data)

export const getGroceryCategories = () =>
	axios.get<GroceryCategory[]>(GET_GROCERY_CATEGORIES_URL)
		.then(res => res.data)

export const getGroceries = () =>
	axios.get<Grocery[]>(GET_GROCERIES_URL)
		.then(res => res.data)

export const getAllRecipes = () =>
	axios.get<Recipe[]>(GET_ALL_RECIPES_URL)
		 .then(res => res.data)

export const getRecipe = (id: number) =>
	axios.get<Recipe>(`${GET_RECIPE_URL}/${id}`)
		.then(res => res.data)

export const getUserRecipes = () =>
	axios.get<Recipe[]>(GET_USER_RECIPES_URL)
		.then(res => res.data)

export const saveGrocery = (body: SaveGroceryRequest) =>
	axios.post<Grocery>(SAVE_GROCERY_URL, body)
		.then(res => res.data)

export const saveRecipe = (body: SaveRecipeRequest) =>
	axios.post<Recipe>(SAVE_RECIPE_URL, body)
		.then(res => res.data)