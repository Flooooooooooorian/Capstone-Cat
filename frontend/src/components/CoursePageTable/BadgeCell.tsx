import React from "react";

export interface BadgeCellProps {
    url: string
}

export default function BadgeCell({url}: BadgeCellProps) {
    return (
        <img alt={url} src={url} onClick={(event) => {
            const target = event.target as typeof event.target & { src: string }
            window.open(target.src, '_blank')
        }}/>
    )
}
