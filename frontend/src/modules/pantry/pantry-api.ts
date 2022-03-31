import { AppConfig } from "../app-config"
import axios from "axios"
import { PantryItem } from "./pantry-models"

const GET_PANTRY_ITEMS_URL = `${AppConfig.apiUrl}/pantry`
const SAVE_PANTRY_ITEM_URL = GET_PANTRY_ITEMS_URL
const DELETE_PANTRY_ITEM_URL = GET_PANTRY_ITEMS_URL

export interface SavePantryRequest {
	grocery: number
	unit: string
	quantity: number
	expiration?: Date
}

export const getPantryItems = () =>
	axios.get<PantryItem[]>(GET_PANTRY_ITEMS_URL)
		.then(res => res.data)

export const savePantryItem = (body: SavePantryRequest) =>
	axios.post<PantryItem>(SAVE_PANTRY_ITEM_URL,
		{
			...body,
			expiration: body.expiration?.toISOString()
		})
		.then(res => res.data)

export const deletePantryItem = (id: number) =>
	axios.delete<number>(`${DELETE_PANTRY_ITEM_URL}/${id}`)
		.then(res => res.data)