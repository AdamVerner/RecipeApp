import { useDeletePantryItem, usePantryItemsDisplay } from "../pantry-queries"
import { Card, IconButton, List, ListItem, ListItemText, Stack, Divider, ListSubheader } from "@mui/material"
import DeleteIcon from "@mui/icons-material/Delete"
import { PantryItemDisplay } from "../pantry-models"
import { AddPantryGroceryForm } from "./AddPantryGroceryForm"
import { useMemo } from "react"
import _ from "lodash"

export const Pantry = () => {
	const pantryItems = usePantryItemsDisplay()
	const { deletePantryItem } = useDeletePantryItem()

	const removePantryItem = (item: PantryItemDisplay) => {

		deletePantryItem(item.id)
	}

	const pantryItemsByCategory = useMemo(() => {
		return _.groupBy(pantryItems, item => item.category)
	}, [pantryItems])

	return (
		<>
			<Stack  alignItems="center">
				<Card sx={{ maxWidth: 600 }}>
					{ Object.entries(pantryItemsByCategory).map(( [category, items]) => (
						<div key={category}>
							<List
								dense
								subheader={
									<ListSubheader>
										{category}
									</ListSubheader>
								}
							>
								{items.map((item, i) => (
									<ListItem
										key={i}
										secondaryAction={
											<IconButton
												onClick={() => removePantryItem(item)}
												edge="end"
												aria-label="delete"
											>
												<DeleteIcon />
											</IconButton>
										}
									>
										<ListItemText
											primary={!item.expiration ?
												item?.name :
												`${item?.name} (${new Date(item.expiration).toLocaleDateString()})`}
											secondary={`${item.quantity} ${item.unit}`}
										/>
									</ListItem>
								))}
							</List>
							<Divider />
						</div>
					))}
					<List>
						<ListItem >
							<AddPantryGroceryForm />
						</ListItem>
					</List>
				</Card>
			</Stack>
		</>
	)
}