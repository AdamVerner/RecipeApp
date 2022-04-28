import { useDeletePantryItem, usePantryItemsDisplay } from "../pantry-queries"
import { Card, IconButton, List, ListItem, ListItemText, Stack, Divider, ListSubheader, Fab } from "@mui/material"
import DeleteIcon from "@mui/icons-material/Delete"
import { PantryItemDisplay } from "../pantry-models"
import { AddPantryGroceryForm } from "./AddPantryGroceryForm"
import { useCallback, useMemo } from "react"
import _ from "lodash"
import { Add } from "@mui/icons-material"
import { useModal } from "mui-modal-provider"

export const Pantry = () => {
	const pantryItems = usePantryItemsDisplay()
	const { deletePantryItem } = useDeletePantryItem()
	const { showModal } = useModal()

	const removePantryItem = (item: PantryItemDisplay) => {
		deletePantryItem(item.id)
	}

	const pantryItemsByCategory = useMemo(() => {
		return _.groupBy(pantryItems, item => item.category)
	}, [pantryItems])

	const handleAddClick = useCallback(() => {
		const modal = showModal(AddPantryGroceryForm, { handleClose: () => { modal.hide() } })
	}, [showModal])

	return (
		<>
			<Stack  alignItems="center" spacing={3}>
				<Card sx={{ minWidth: 450 }} >
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
											secondary={`${item.quantity ?? ""} ${item.unit ?? ""}`}
										/>
									</ListItem>
								))}
							</List>
							<Divider />
						</div>
					))}
				</Card>
				<Fab color="primary" aria-label="add" onClick={handleAddClick}>
					<Add />
				</Fab>
			</Stack>
		</>
	)
}