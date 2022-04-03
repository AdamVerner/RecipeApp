import { RecipeList } from "./RecipeList"
import { Fab, InputAdornment, Stack, TextField } from "@mui/material"
import { Search, Add } from "@mui/icons-material"
import { useState, KeyboardEvent, useEffect, useCallback } from "react"
import { useModal } from "mui-modal-provider"
import { RecipeCreateDialog } from "./RecipeCreateDialog"
import { Recipe } from "../recipe-models"

interface RecipeBrowserProps {
	recipes: Recipe[]
}



export const RecipeBrowser = ({ recipes } : RecipeBrowserProps) => {
	const { showModal } = useModal()

	const [searchText, setSearchText] = useState("")
	const [filteredRecipes, setFilteredRecipes] = useState(recipes)

	const filterRecipes = useCallback(() => {
		const search = searchText.toLowerCase()
		setFilteredRecipes(recipes.filter(recipe => recipe.name.toLowerCase().includes(search)))
	}, [searchText, recipes])

	useEffect(() => {
		filterRecipes()
	}, [recipes, filterRecipes])

	const handleKeyPress = (e: KeyboardEvent<HTMLDivElement>) => {
		if (e.key == "Enter") {
			filterRecipes()
		}
	}

	const handleAddClick = () => {
		const modal = showModal(RecipeCreateDialog, { handleClose: () => { modal.hide() } })
	}

	return (
		<Stack spacing={3} alignItems="center">
			<TextField
				label="Search"
				inputMode="search"
				variant="outlined"
				InputProps={{
					startAdornment: (
						<InputAdornment position="end">
							<Search />
						</InputAdornment>
					)
				}}
				onKeyPress={handleKeyPress}
				onChange={(e) => setSearchText(e.target.value)}
			/>
			<RecipeList recipes={filteredRecipes}/>
			<Fab color="primary" aria-label="add" onClick={handleAddClick}>
				<Add />
			</Fab>
		</Stack>
	)
}