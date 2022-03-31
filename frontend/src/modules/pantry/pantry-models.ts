
export interface PantryItem {
	id: number
	grocery: number
	unit: string
	quantity: number
	expiration?: string
}

export interface PantryItemDisplay extends Omit<PantryItem, "expiration">{
	name: string
	category: string
	expiration?: Date
}