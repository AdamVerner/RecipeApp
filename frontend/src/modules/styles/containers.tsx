import { Stack, StackProps } from "@mui/material"

export const TextStack = (props: StackProps) => (
	<Stack spacing={1} {...props} direction="row" alignItems="center">{props.children}</Stack>
)